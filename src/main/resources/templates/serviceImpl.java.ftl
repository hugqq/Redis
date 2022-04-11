package ${package.ServiceImpl};


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};

/**
 * <p>
 * ${table.comment} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */

@Transactional
@Service
public class ${table.serviceImplName} extends ServiceImpl<${table.mapperName}, ${entity}> implements ${table.serviceName} {

}

