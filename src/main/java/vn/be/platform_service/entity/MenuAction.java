package vn.be.platform_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "menu_action")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "seq_generator", sequenceName = "MENU_ACTION_SEQ", allocationSize = 1)
public class MenuAction extends BaseEntity {

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "action_id", nullable = false)
    private Long actionId;

    @Column(name = "action_code", length = 100)
    private String actionCode;
}
