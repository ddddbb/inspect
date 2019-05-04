package com.wanglin.web.controller;

import com.wanglin.common.annotation.Log;
import com.wanglin.common.base.AjaxResult;
import com.wanglin.common.enums.BusinessType;
import com.wanglin.common.page.TableDataInfo;
import com.wanglin.common.support.Convert;
import com.wanglin.common.utils.poi.ExcelUtil;
import com.wanglin.domain.Rule;
import com.wanglin.domain.RuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rule")
public class RuleController extends BaseController {
    private String prefix = "config/rule";

    @Autowired
    RuleMapper mapper;

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
    public TableDataInfo list(Rule config)
    {
        startPage();
        List<Rule> list = mapper.selectRule(config);
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
    public AjaxResult addSave(Rule config)
    {
        config.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(mapper.insertRule(config));
    }

    /**
     * 修改参数配置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long configId, ModelMap mmap)
    {
        mmap.put("rule", mapper.get(configId));
        return prefix + "/edit";
    }
    /**
     * 修改保存参数配置
     */
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Rule config)
    {
        config.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(mapper.updateRule(config));
    }

    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Rule config)
    {
        List<Rule>     list = mapper.selectRule(config);
        ExcelUtil<Rule> util = new ExcelUtil<Rule>(Rule.class);
        return util.exportExcel(list, "结构数据");
    }

    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(mapper.disableRules(     Convert.toLongArray(ids)));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

}
