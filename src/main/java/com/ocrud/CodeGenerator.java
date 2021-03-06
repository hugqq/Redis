package com.ocrud;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.ocrud.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CodeGenerator {

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/redis?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT%2B8";
    private static final String USERNAME = "root";
    private static final String PASSWD = "root";
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String[] SUPER_ENTITY_COLUMNS = {"id", "remarks", "create_by", "create_date", "update_by", "update_date", "del_flag"};
    private static final String PACKAGE_NAME = "com.ocrud";
    private static final String MODEL_NAME = "";
    private static final String[] TAB_NAME = {"t_restaurants"};
    private static final String AUTHOR = "ocrud";
    private static final String PROJECT_PATH = "C:\\Users\\33047\\IdeaProjects\\Redis\\src\\main\\java";


    public static void main(String[] args) {
        generateByTables();
    }

    private static void generateByTables() {
        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();

        // 全局配置
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(true)
                .setIdType(IdType.ASSIGN_UUID)
                .setAuthor(CodeGenerator.AUTHOR)
                .setOutputDir(PROJECT_PATH)
                .setOpen(false)
                .setFileOverride(true)
                .setServiceName("%sService");
        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(DB_URL)
                .setUsername(USERNAME)
                .setPassword(PASSWD)
                .setDriverName(DRIVER_NAME);
        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig
                .setModuleName(MODEL_NAME)
                .setParent(PACKAGE_NAME)
                .setController("controller")
                .setService("service")
                .setEntity("entity");

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig() {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return PROJECT_PATH + "/src/main/resources/mapper/" + MODEL_NAME
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        // 配置自定义输出模板
        TemplateConfig templateConfig = new TemplateConfig()
                .setController("templates/controller.java")
                .setEntity("templates/entity.java")
                .setService("templates/service.java")
                .setServiceImpl("templates/serviceImpl.java")
                .setMapper("templates/mapper.java")
                .setXml("templates/mapper.xml");

        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setSuperEntityClass(BaseEntity.class)
                .setEntityLombokModel(true)
                .setRestControllerStyle(true)
                .setTablePrefix("t_")
                // 写于父类中的公共字段
                .setSuperEntityColumns(SUPER_ENTITY_COLUMNS)
                .setCapitalMode(false)
                .setControllerMappingHyphenStyle(true)
                // 修改替换成你需要的表名，多个表名传数组
                .setInclude(TAB_NAME)
                .setSuperEntityClass(BaseEntity.class.getName())
                .setSuperMapperClass(BaseMapper.class.getName())
                .setSuperServiceClass(IService.class.getName())
                .setSuperServiceImplClass(ServiceImpl.class.getName())
        ;

        autoGenerator
                .setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(packageConfig)
                .setTemplateEngine(new FreemarkerTemplateEngine())
                .setTemplate(templateConfig).execute();

        log.info("=======================Java代码生成成功=======================");
    }
}
