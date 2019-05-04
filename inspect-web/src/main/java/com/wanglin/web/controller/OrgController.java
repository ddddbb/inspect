package com.wanglin.web.controller;

import com.wanglin.common.annotation.Log;
import com.wanglin.common.base.AjaxResult;
import com.wanglin.common.enums.BusinessType;
import com.wanglin.common.page.TableDataInfo;
import com.wanglin.common.support.Convert;
import com.wanglin.common.utils.poi.ExcelUtil;
import com.wanglin.domain.Org;
import com.wanglin.domain.OrgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/org")
public class OrgController extends BaseController {
    private String prefix = "config/org";

    @Autowired
    OrgMapper orgMapper;

    @GetMapping()
    public String list()
    {
        return prefix + "/data";
    }
    /**
     * 查询参数配置列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Org config)
    {
        startPage();
        List<Org> list = orgMapper.selectOrg(config);
        return getDataTable(list);
    }


    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }


    /**
     * 新增保存参数配置
     */
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Org config)
    {
        config.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(orgMapper.insertOrg(config));
    }

    /**
     * 修改参数配置
     */
    @GetMapping("/edit/{orgCode}")
    public String edit(@PathVariable("orgCode") String configId, ModelMap mmap)
    {
        mmap.put("org", orgMapper.get(configId));
        return prefix + "/edit";
    }
    /**
     * 修改保存参数配置
     */
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Org config)
    {
        config.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(orgMapper.updateOrg(config));
    }

    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Org config)
    {
        List<Org>      list = orgMapper.selectOrg(config);
        ExcelUtil<Org> util = new ExcelUtil<Org>(Org.class);
        return util.exportExcel(list, "结构数据");
    }

    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(orgMapper.disableOrgs(     Convert.toStrArray(ids)));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

}

