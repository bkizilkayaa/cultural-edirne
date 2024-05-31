package com.bkizilkaya.culturelbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ACTION_LOG")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ActionLog {
    @Id
    @GeneratedValue(generator = "genActionLogSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "genActionLogSeq", sequenceName = "SEQ_ACTION_LOG", initialValue = 100, allocationSize = 11)
    private Long Id;

    @Column(name = "USER")
    private String user;

    @Column(name = "ACTION")
    private String action;

    @Column(name = "TABLE_NAME")
    private String tableName;

    @Column(name = "CREATE_DATE")
    @CreationTimestamp
    private LocalDateTime timestamp;

    @Column(name = "LOG_DETAIL", length = 100000)
    @Lob
    private String logDetail;

}
