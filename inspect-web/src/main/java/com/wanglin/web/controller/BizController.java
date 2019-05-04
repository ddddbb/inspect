package com.wanglin.web.controller;

import com.wanglin.common.base.AjaxResult;
import com.wanglin.common.page.TableDataInfo;
import com.wanglin.common.support.Convert;
import com.wanglin.domain.Biztype;
import com.wanglin.domain.BiztypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/biztype")
public class BizController extends BaseController {
    private String prefix = "config/biztype";
    @Autowired
    BiztypeMapper biztypeMapper;

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
    public TableDataInfo list(Biztype config)
    {
        startPage();
        List<Biztype> list = biztypeMapper.selectBiztype(config);
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
    public AjaxResult addSave(Biztype config)
    {
        config.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(biztypeMapper.insertBiztype(config));
    }

    /**
     * 修改参数配置
     */
    @GetMapping("/edit/{code}")
    public String edit(@PathVariable("code") String configId, ModelMap mmap)
    {
        mmap.put("biztype", biztypeMapper.get(configId));
        return prefix + "/edit";
    }
    /**
     * 修改保存参数配置
     */
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Biztype config)
    {
        config.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(biztypeMapper.updateBiztype(config));
    }



    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(biztypeMapper.disableBiztypes(     Convert.toStrArray(ids)));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }
}
