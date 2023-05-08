# java-seed

## structure

**java-seed**
- **backoffice**

## Version
JDK: corretto-17

Other:

|   framework   |   version   |
| ---- | ---- |
|   Gradle   |   7.4.2   |
|   Spring-boot   |   2.7.2   |

## Add java formatter file to your local git
[config instruction ](config/README/README.md)

# Code style standard
## Constraint
1. [Add java formatter file to your local git](config/README/README.md);
2. Install alibaba java coding guidelines;
3. [API design follows the RESTful style](doc/RESTFUL-STANDARD.md);
4. Don't return any model entities in service;
5. Please set an initial capacity while you are creating a collection type or map type container;
6. Don't use a single constant class to maintain all constants;
7. Using deprecated methods is forbidden;
8. Use `equals()` method to compare `Integer`&`Long`;
9. Use `compareTo()` method to compare `BigDecimal`;
10. Using the constructor `BigDecimal(double)` to create `BigDecimal` object is forbidden;
11. Using `xxUtils` to judge if collection or map is empty;
12. Set a meaningful thread name if you are trying to create a thread or thread pool;
13. Recycle custom `ThreadLocal` variables;
14. Add `@Deprecated` on deprecated function with annotation about operator and planned time of deletion
15. `Enum` values should all be defined capitally
16. Add `TODO` on an uncompleted function with operator and planned resolve time
17. TBD

## Recommended
1. TBD
