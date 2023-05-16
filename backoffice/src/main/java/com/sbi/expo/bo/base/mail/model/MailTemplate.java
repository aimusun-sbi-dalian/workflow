package com.sbi.expo.bo.base.mail.model;

import com.sbi.expo.bo.base.jpa.model.Model;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mgt_mail_template")
public class MailTemplate extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "keyword", unique = true, nullable = false)
    @NotBlank
    private String keyword;

    @Column(name = "parameters")
    private String parameters;

    @Column(name = "description")
    private String description;

    @Column(name = "subject", nullable = false)
    @NotBlank
    private String subject;

    @Column(name = "content", nullable = false)
    @NotBlank
    private String content;

    @Column(name = "charset", nullable = false)
    @NotBlank
    private String charset;
}
