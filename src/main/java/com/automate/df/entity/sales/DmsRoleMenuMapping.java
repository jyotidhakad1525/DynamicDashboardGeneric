package com.automate.df.entity.sales;

import com.automate.df.entity.sales.employee.DmsRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "dms_role_menu_mapping")
@NamedQuery(name = "DmsRoleMenuMapping.findAll", query = "SELECT d FROM DmsRoleMenuMapping d")
public class DmsRoleMenuMapping implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "branch_id")
    private Integer branchId;

    @Column(name = "menu_id")
    private Integer menuId;

    @Column(name = "organization_id")
    private Integer organizationId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private DmsRole dmsRole;

    @ManyToOne
    @JoinColumn(name = "action_id")
    private DmsAction dmsAction;

}