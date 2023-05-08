
## <a name="api"></a>API设计

### URL规约

所有API的URL必须具有RESTful风格

- 使用名词而不是动词表示资源
- 使用小写单数形式表示资源
- 使用路径参数表示资源标识符
- 使用版本控制管理 API 版本
- 对某个资源进行全量查询，使用`/all`后缀来表示
- 使用`/pub/`和`/int/`标识符来管理资源是否对外暴露

例如：

```
GET    /int/v1/user         // 获取用户列表(分页查询)
GET    /int/v1/user/all     // 获取所有的用户
POST   /int/v1/user         // 创建新用户
GET    /int/v1/user/{id}    // 获取特定用户的详细信息
PUT    /int/v1/user/{id}    // 更新特定用户的信息
DELETE /int/v1/user/{id}    // 删除特定用户
```

- 对于嵌套资源，使用嵌套URL，例如：


```
GET    /int/v1/user/{id}/order         // 获取特定用户的订单列表(分页查询)
GET    /int/v1/user/{id}/order/all     // 获取特定用户的所有订单
POST   /int/v1/user/{id}/order         // 为特定用户创建新订单
GET    /int/v1/user/{id}/order/{id}    // 获取特定用户的特定订单
PUT    /int/v1/user/{id}/order/{id}    // 更新特定用户的特定订单
DELETE /int/v1/user/{id}/order/{id}    // 删除特定用户的特定订单
```

- 使用查询参数表示过滤和排序，例如:获取状态为 active 的用户，并按照创建时间排序。
```
  /int/v1/user?status=active&sort=createdAt
```

- 多单词连接时使用`-`作为连接符
```
  /int/v1/special-customer
```

- 参数名字可以使用驼峰方式命名
```
  /int/v1/address?zipCode=6100001
```

- 使用 `/content` 后缀来表示接口资源是文件
```
POST   /int/v1/document/content          // 上传
GET    /int/v1/document/{id}/content     // 下载
PUT    /int/v1/document/{id}/content     // 更改
DELETE /int/v1/document/{id}/content     // 删除
```

### 应答处理

#### 非异常

- 成功 HTTP STATUS 是 200
  - code 字段为常量 1
  - data 为返回数据
  - 分页场景使用Spring的分页标准
```
{
"code": 1,    
"msg": "ok",   
"data": {},         // response 数据
"errors":  null     // response 数据 为 error数据
}
```
#### 异常
异常响应应优先根据 Http Status 的默认含义进行匹配

- 非成功 （HTTP STATUS 不是 200）
  - 400 BAD REQUEST: 一般指调用接口的方式、参数有错误
  - 401 UNAUTHORIZED: 认证失败
  - 403 FORBIDDEN: 鉴权失败
  - 404 NOT FOUND: 资源不存在
  - 500 INTERNAL SERVER ERROR: 服务端内部错误


- 如需包含更具体的异常信息，可包含如下字段：
    - data 为 null
    - code 字段为在业务层下，具有唯一性的标识符常量，如 10100
    - msg 是服务端对于该异常的文言描述
    - errors 是异常所包含的元数据，其类型为数组
```
{
"code": 10100,     // 后端自定义code
"msg": "Request parameter exception.",     // 后端自定义code 对应的message
"data": null,      // response 数据
"errors": {}       //  response 数据 为 error数据
}
```
- 各种 field ，如果没有，统一反 null




---

## 数据库设计

1. 表名是名词的单数形式，使用下划线分隔单词，例如`user`、`post`、`special_customer`。
2. 每个表必须有一个自增长的主键ID，用id命名，不要使用`xxxx_id`来命名，例如: `user_id`。
3. 每个表必须有一个`version`, `created_at`和`updated_at`字段，用于跟踪记录的创建和更新时间。
4. 对于一些有逻辑删除业务需求的表，需要有一个`deleted`字段，用于记录是否被逻辑删除。
5. 在`Java`中`Boolean`类型字段，在数据库中使用`Tinyint`类型定义。
6. 对于多对多关系，必须使用中间表，例如：
```
user
- id (primary key)
- name
- email_address

group
- id (primary key)
- name

user_group
- user_id (foreign key to user.id)
- group_id (foreign key to group.id)
```

使用Spring JPA连接数据库的建议

- 定义实体类来映射数据库表。
- 使用JPA注解来定义实体类与数据库表之间的映射关系。
- 使用`JpaRepository`接口来实现常见的CRUD操作。

---

## 包结构

每个包代表一个功能或模块，每个功能包结构包含控制层，业务逻辑层，数据库访问层。
例如：
```
staff
  ┣ controller
  ┣ dto
  ┣ form
  ┣ model
  ┣ repository
  ┗ service
 
 group
  ┣ controller
  ┣ dto
  ┣ form
  ┣ model
  ┣ repository
  ┗ service
```
## 类设计

所有服务类必须遵循单一职责原则，即每个类只负责一个任务。
1. 每个类只负责一个数据实体的操作，尽量避免一个类处理多个实体或多个操作。
2. 类名应当与数据实体名称相对应，具有语义上的连贯性，易于理解。
3. 在进行异常处理时，应当采用统一的异常处理机制，避免在类中使用 try-catch 块处理异常，以保证代码的可读性和可维护性。


### Controller层

1. 使用合适的HTTP方法。参考: [API设计](#api)
2. 使用合适的URL路径。参考: [API设计](#api)
3. 在处理请求响应时，和前端约定好，统一使用JSON格式 (`Accept: application/json; Content-Type: application/json`)。
4. 使用 `@ControllerAdvice` 和 `@ExceptionHandler` 处理各种异常情况。
5. 使用 `@PreAuthorize` 注解进行访问控制。

### Service层

在Service层中，为每个实体对象创建一个Service接口，并提供以下方法：

- 创建：EntityDto create(EntityForm form)
- 读取：EntityDto findById(long id)
- 更新：void update(EntityDto entityDto)
- 删除：void delete(long id)

### DAO层

1. 在定义`Repository`时，需要遵循单一职责原则，即一个`Repository`只负责一个实体类的操作。
2. 在书写查询语句时，尽量使用JPA的内置方法，如`findByXxx`、`findAllByXxx`等，避免使用原生SQL语句。
3. 如果需要使用原生SQL语句，尽量使用`JdbcTemplate`，尽量不要使用`HQL`或是`JPQL`。

### DTO对象

1. 在进行DTO的转换时，可以使用`MapStruct`来进行对象属性的拷贝和赋值，而不要使用`BeanUtils`。
2. DTO通常是不应该包含业务逻辑的，DTO的作用仅仅是传输数据，应该避免在DTO中定义复杂的计算和判断逻辑。
3. 在进行DTO的数据处理时，应该考虑数据的安全性和保密性，避免数据泄露和不当使用。
4. 如果定义的字段有枚举类型，则使用枚举类型。

### Model对象

1. 在定义实体类属性时，应该考虑数据的类型、长度和约束等，遵循数据库的规范和实际业务需求。
2. 使用`Bean Validation`注解来验证Model中的属性值，以确保数据的有效性和合法性。
3. 在实体类之间定义合适的关系映射，如一对一(`@OneToOne`)、一对多(`@OneToMany`)和多对多(`@ManyToMany`)等，以便于进行复杂的查询和操作。


---

