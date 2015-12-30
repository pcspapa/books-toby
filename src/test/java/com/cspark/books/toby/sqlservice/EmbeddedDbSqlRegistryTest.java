package com.cspark.books.toby.sqlservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

/**
 * @Transactional을 위히여 Override을 이용하여 super를 호출함.
 * (Override 하지 않으면 Rollback 안됨.)
 *
 * Created by cspark on 2015. 12. 29..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-sqlmap-applicationContext.xml")
@Transactional
public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    UpdatableSqlRegistry updatableSqlRegistry;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        return updatableSqlRegistry;
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

    /**
     * Transactional이 Test와 실행 오브젝트에 같이 있을 경우 작동이 안됨.
     * @throws Exception
     */
    @Test
    public void transactionalUpdate() throws Exception {
        super.checkFindResult("SQL1", "SQL2", "SQL3");

        Map<String, String> sqlMap = new HashMap<>();
        sqlMap.put("KEY1", "Modifier1");
        sqlMap.put("SQL9999!@#$", "Modifier9999");

        try {
            updatableSqlRegistry.updateSql(sqlMap);
            fail();
        } catch (SqlUpdateFailureException e) {
        }

        super.checkFindResult("SQL1", "SQL2", "SQL3");
    }
}
