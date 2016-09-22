package xyz.fz.domain.role;

import javax.persistence.*;

/**
 * Created by fz on 2016/9/11.
 */
@Entity()
@Table(name = "t_auth")
public class TAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false, length = 50)
    private String text;

    @Column(nullable = false, length = 100)
    private String url;

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

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
