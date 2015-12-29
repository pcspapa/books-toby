package com.cspark.books.toby.sqlservice;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * @Transactional을 위히여 Override을 이용하여 super를 호출함.
 * (Override 하지 않으면 Rollback 안됨.)
 *
 * Created by cspark on 2015. 12. 29..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/sqlmap-applicationContext.xml")
@Transactional
public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

    @Autowired
    DataSource dataSource;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {

        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(dataSource);
        return sqlRegistry;
    }

    @Override
    public void find() throws Exception {
        super.find();
    }

    @Override
    public void unknownKey() throws Exception {
        super.unknownKey();
    }

    @Override
    public void updateSingle() throws Exception {
        super.updateSingle();
    }

    @Override
    public void updateMulti() throws Exception {
        super.updateMulti();
    }

    @Override
    public void updateWithNotExistException() throws Exception {
        super.updateWithNotExistException();
    }
}
