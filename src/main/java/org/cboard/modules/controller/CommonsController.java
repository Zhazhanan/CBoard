package org.cboard.modules.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import org.cboard.modules.dto.DashboardMenu;
import org.cboard.modules.dto.User;
import org.cboard.modules.services.AdminSerivce;
import org.cboard.modules.services.MenuService;
import org.cboard.modules.services.PersistService;
import org.cboard.modules.services.ServiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yfyuan on 2016/7/25.
 */
@RestController
@RequestMapping("/commons")
@Api(tags = {"Commons"}, description = "Manage User、Menu、changePwd、persist")
public class CommonsController extends BaseController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private AdminSerivce adminSerivce;

    @Autowired
    private PersistService persistService;

    @RequestMapping(value = "/getUserDetail")
    public User getUserDetail() {
        return authenticationService.getCurrentUser();
    }

    @RequestMapping(value = "/getMenuList")
    public List<DashboardMenu> getMenuList() {
        return menuService.getMenuList();
    }

    @RequestMapping(value = "/changePwd")
    public ServiceStatus changePwd(@RequestParam(name = "curPwd") String curPwd, @RequestParam(name = "newPwd") String newPwd, @RequestParam(name = "cfmPwd") String cfmPwd) {
        return adminSerivce.changePwd(tlUser.get().getUserId(), curPwd, newPwd, cfmPwd);
    }

    @RequestMapping(value = "/persist")
    public String persist(@RequestBody String dataStr) {
        JSONObject data = JSONObject.parseObject(dataStr);
        return persistService.persistCallback(data.getString("persistId"), data.getJSONObject("data"));
    }

}
