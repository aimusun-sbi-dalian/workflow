package com.sbi.dl.template.repository;

import com.sbi.dl.template.model.Foo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
public interface FooRepository
        extends JpaRepository<Foo, Long>, JpaSpecificationExecutor<Foo> {}
