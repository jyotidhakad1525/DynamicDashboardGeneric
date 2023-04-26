package com.automate.df.entity.sales;

import com.automate.df.entity.oh.DmsBranch;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_action")
@NamedQuery(name = "DmsAction.findAll", query = "SELECT d FROM DmsAction d")
public class DmsAction implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_id")
    private int actionId;

    @Column(name = "action_name")
    private String actionName;

    @Column(name = "api_url")
    private String apiUrl;

    private String description;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "editapi_url")
    private String editapiUrl;

    @Column(name = "render_type")
    private String renderType;

    //bi-directional many-to-one association to DmsOrganization
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private DmsOrganization dmsOrganization;

    //bi-directional many-to-one association to DmsBranch
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private DmsBranch dmsBranch;

    //bi-directional many-to-one association to DmsMenu
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private DmsMenu dmsMenu;

    //bi-directional many-to-one association to DmsRoleMenuMapping
    @OneToMany(mappedBy = "dmsAction")
    private List<DmsRoleMenuMapping> dmsRoleMenuMappings;

    public DmsRoleMenuMapping addDmsRoleMenuMapping(DmsRoleMenuMapping dmsRoleMenuMapping) {
        getDmsRoleMenuMappings().add(dmsRoleMenuMapping);
        dmsRoleMenuMapping.setDmsAction(this);

        return dmsRoleMenuMapping;
    }

    public DmsRoleMenuMapping removeDmsRoleMenuMapping(DmsRoleMenuMapping dmsRoleMenuMapping) {
        getDmsRoleMenuMappings().remove(dmsRoleMenuMapping);
        dmsRoleMenuMapping.setDmsAction(null);

        return dmsRoleMenuMapping;
    }

}