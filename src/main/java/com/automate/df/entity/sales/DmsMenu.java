package com.automate.df.entity.sales;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_menu")
@NamedQuery(name = "DmsMenu.findAll", query = "SELECT d FROM DmsMenu d")
public class DmsMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Integer menuId;

    @Column(name = "api_url")
    private String apiUrl;

    @Column(name = "branch_id")
    private Integer branchId;

    private String description;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "editapi_url")
    private String editapiUrl;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_type")
    private String menuType;

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "parent_menu")
    private String parentMenu;

    //bi-directional many-to-one association to DmsAction
    @OneToMany(mappedBy = "dmsMenu")
    private List<DmsAction> dmsActions;

    public DmsAction addDmsAction(DmsAction dmsAction) {
        getDmsActions().add(dmsAction);
        dmsAction.setDmsMenu(this);

        return dmsAction;
    }

    public DmsAction removeDmsAction(DmsAction dmsAction) {
        getDmsActions().remove(dmsAction);
        dmsAction.setDmsMenu(null);

        return dmsAction;
    }

}