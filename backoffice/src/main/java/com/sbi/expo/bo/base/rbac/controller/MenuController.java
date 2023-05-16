package com.sbi.expo.bo.base.rbac.controller;

import com.sbi.expo.bo.base.ResponseBase;
import com.sbi.expo.bo.base.rbac.dto.MenuTreeDTO;
import com.sbi.expo.bo.base.rbac.form.MenuForm;
import com.sbi.expo.bo.base.rbac.service.MenuService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * MenuController
 *
 * @author Ming.G
 * @date 2022-07-28
 */
@RestController
@RequestMapping("/int/v1/menu")
public class MenuController {

    @Autowired private MenuService menuService;

    /**
     * get all menu tree
     *
     * @author Ming.G
     * @date 2022-10-11 16:22
     */
    @GetMapping("/tree")
    @PreAuthorize("@auth.check('sys:menu:read')")
    public ResponseBase<List<MenuTreeDTO>> getMenuTree() {
        return ResponseBase.ok(menuService.getMenuTree());
    }

    @PostMapping("")
    @PreAuthorize("@auth.check('sys:menu:create')")
    public ResponseBase<Void> create(@RequestBody @Valid MenuForm form) {
        menuService.create(form);
        return ResponseBase.ok();
    }

    @PutMapping("/{id}")
    @PreAuthorize("@auth.check('sys:menu:modify')")
    public ResponseBase<Void> modify(
            @PathVariable("id") Long menuId, @RequestBody @Valid MenuForm form) {
        menuService.modify(menuId, form);
        return ResponseBase.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@auth.check('sys:menu:delete')")
    public ResponseBase<Void> delete(@PathVariable("id") Long menuId) {
        menuService.delete(menuId);
        return ResponseBase.ok();
    }
}
