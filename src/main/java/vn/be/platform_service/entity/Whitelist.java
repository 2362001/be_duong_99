package vn.be.platform_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WHITELIST_URLS", schema = "DEMO")
@EntityListeners(AuditingEntityListener.class)
public class Whitelist {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_whitelist_urls")
    @SequenceGenerator(name = "seq_whitelist_urls", sequenceName = "seq_whitelist_urls", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String pattern;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Boolean enabled = true;
}
