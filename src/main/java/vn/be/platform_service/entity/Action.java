package vn.be.platform_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "actions")
@SequenceGenerator(name = "seq_generator", sequenceName = "ACTIONS_SEQ", allocationSize = 1)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Action extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "allow_paths", length = 4000)
    private String allowPaths;

    @Column(name = "log_paths", length = 4000)
    private String logPaths;

    @Column(nullable = false)
    private Integer status = 1; // 1=active, 0=inactive

    @Column(length = 1000)
    private String description;
}
