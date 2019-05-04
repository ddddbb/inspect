package com.wanglin.web.controller;

import com.wanglin.common.base.AjaxResult;
import com.wanglin.common.page.TableDataInfo;
import com.wanglin.common.support.Convert;
import com.wanglin.common.utils.poi.ExcelUtil;
import com.wanglin.domain.Data;
import com.wanglin.domain.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/data")
public class DataController extends BaseController {
    private String prefix = "config/data";

    @Autowired
    DataMapper mapper;

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
    public TableDataInfo list(Data config)
    {
        startPage();
        List<Data> list = mapper.selectData(config);
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
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Data config)
    {
        config.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(mapper.insertData(config));
    }

    /**
     * 修改参数配置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long configId, ModelMap mmap)
    {
        mmap.put("data", mapper.get(configId));
        return prefix + "/edit";
    }
    /**
     * 修改保存参数配置
     */
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Data config)
    {
        config.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(mapper.updateData(config));
    }

    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Data config)
    {
        List<Data>      list = mapper.selectData(config);
        ExcelUtil<Data> util = new ExcelUtil<Data>(Data.class);
        return util.exportExcel(list, "结构数据");
    }

    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(mapper.deleteDatas(     Convert.toLongArray(ids)));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }
}
