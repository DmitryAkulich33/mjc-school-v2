package com.epam.esm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Certificate {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
    private Integer state;
    private Integer duration;
    private List<Tag> tags;

    public static CertificateBuilder builder() {
        return new CertificateBuilder();
    }

    public static class CertificateBuilder {
        private Long id;
        private String name;
        private String description;
        private Double price;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime creationDate;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime lastUpdateDate;

        private Integer state;
        private Integer duration;
        private List<Tag> tags;

        public CertificateBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CertificateBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CertificateBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CertificateBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public CertificateBuilder creationDate(LocalDateTime creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public CertificateBuilder lastUpdateDate(LocalDateTime lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public CertificateBuilder state(Integer state) {
            this.state = state;
            return this;
        }

        public CertificateBuilder duration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public CertificateBuilder tags(final List<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public Certificate build() {
            return new Certificate(
                    this.id,
                    this.name,
                    this.description,
                    this.price,
                    this.creationDate,
                    this.lastUpdateDate,
                    this.state,
                    this.duration,
                    this.tags
            );
        }
    }
}
