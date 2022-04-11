package ${package.Controller};


import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


import ${package.Entity}.${ entity};
import ${package.Service}.${table.serviceName};

/**
 * @author ${author}
 * @since ${date}
 */

@RestController
@RequestMapping("${entity?uncap_first}")
@Slf4j
public class ${table.controllerName}  {

    private final ${table.serviceName} ${table.serviceName?uncap_first};

    @Autowired
    public ${table.controllerName}(${table.serviceName} ${table.serviceName?uncap_first}){
        this.${table.serviceName?uncap_first} = ${table.serviceName?uncap_first};
    }

    @GetMapping("pageList")
    public Dict pageList(${entity} ${entity?uncap_first},
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest request)
    {
        Dict r = Dict.create();
        LambdaQueryWrapper<${entity}> lambdaQueryWrapper = new LambdaQueryWrapper<${entity}>();
        Page<${entity}> page = ${table.serviceName?uncap_first}.page(new Page<>(pageNo, pageSize), lambdaQueryWrapper);
        r.put("data", page);
        return r;
    }

    @GetMapping("getOne")
    public Dict getOne(${entity} ${entity?uncap_first}, HttpServletRequest request)
    {
        Dict r = new Dict();
        LambdaQueryWrapper<${entity}> lambdaQueryWrapper = new LambdaQueryWrapper<${entity}>();
        ${entity} one = ${table.serviceName?uncap_first}.getOne(lambdaQueryWrapper);
        r.put("data", one);
        return r;
    }

    @GetMapping("getById")
    public Dict getByid(String id, HttpServletRequest request)
    {
        Dict r = Dict.create();
        ${entity} one = ${table.serviceName?uncap_first}.getById(id);
        r.put("data", one);
        return r;
    }

                                        
    @PostMapping("saveOrUpdate")
    public Dict saveOrUpdate(${entity} ${entity?uncap_first}, HttpServletRequest request)
    {
        Dict r = Dict.create();
        if (${table.serviceName?uncap_first}.saveOrUpdate(${entity?uncap_first})) {
            ${entity} one = ${table.serviceName?uncap_first}.getById(${entity?uncap_first}.getId());
            r.put("data", one);
            return r;
        }
        return null;
    }

    @DeleteMapping("del")
    public Dict del(String id, HttpServletRequest request)
    {
        Dict r = Dict.create();
        r.put("data", ${table.serviceName?uncap_first}.removeById(id));
        return r;
    }

}


