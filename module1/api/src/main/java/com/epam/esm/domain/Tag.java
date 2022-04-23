package com.epam.esm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//@NoArgsConstructor
public class Tag {
    private Long id;
    private String name;
    private Integer state;

    public static TagBuilder builder() {
        return new TagBuilder();
    }

    public static class TagBuilder {
        private Long id;
        private String name;
        private Integer state;

        public TagBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TagBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TagBuilder state(Integer state) {
            this.state = state;
            return this;
        }

        public Tag build() {
            return new Tag(this.id, this.name, this.state);
        }
    }
}
