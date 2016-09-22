package xyz.fz.domain.role;

import javax.persistence.*;

/**
 * Created by fz on 2016/9/15.
 */
@Entity()
@Table(name = "t_menu")
public class TMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    private String menuName;

    @Column(nullable = false, length = 50)
    private String menuPath;

    @Column(nullable = false, columnDefinition = "int(1) not null")
    private Integer isActivity;

    @Column(nullable = false, columnDefinition = "int(4) not null")
    private Integer sort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    public Integer getIsActivity() {
        return isActivity;
    }

    public void setIsActivity(Integer isActivity) {
        this.isActivity = isActivity;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
