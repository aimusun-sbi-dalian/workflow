package com.sbi.expo.bo.base.rbac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sbi.expo.bo.base.jpa.model.DeletableModel;
import java.util.List;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

/**
 * Menu
 *
 * @author Ming.G
 * @date 2022-07-28
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "mgt_menu")
@Where(clause = "deleted = 0")
public class Menu extends DeletableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private List<Menu> children;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "pid", insertable = false, updatable = false)
    private Menu parent;

    private Long pid;
    private String name;
    private int sort;
    /** For front-end use only, provided by the front-end when adding new menu */
    private String menuKey;

    private String routing;
    private Boolean visible;

    @JsonIgnore
    @ManyToMany(mappedBy = "menus", fetch = FetchType.LAZY)
    private List<Role> roles;

    @Builder
    public Menu(
            boolean deleted,
            Long createdAt,
            Long updatedAt,
            Integer version,
            Long id,
            List<Menu> children,
            Menu parent,
            Long pid,
            String name,
            int sort,
            String routing,
            Boolean visible,
            List<Role> roles) {
        super(deleted, createdAt, updatedAt, version);
        this.id = id;
        this.children = children;
        this.parent = parent;
        this.pid = pid;
        this.name = name;
        this.sort = sort;
        this.routing = routing;
        this.visible = visible;
        this.roles = roles;
    }
}
