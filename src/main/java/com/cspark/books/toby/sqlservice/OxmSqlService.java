package com.cspark.books.toby.sqlservice;

import com.cspark.books.toby.dao.UserDao;
import com.cspark.books.toby.sqlservice.jaxb.SqlType;
import com.cspark.books.toby.sqlservice.jaxb.Sqlmap;
import org.springframework.oxm.Unmarshaller;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

/**
 * Created by cspark on 2015. 12. 29..
 */
public class OxmSqlService implements SqlService {

    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlmapFile(String sqlmapFile) {
        oxmSqlReader.setSqlmapFile(sqlmapFile);
    }

    @PostConstruct
    public void loadSql() {
        oxmSqlReader.read(sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }

    private class OxmSqlReader implements SqlReader {

        public static final String DEFAULT_SQLMAP_FILE = "sqlmap.xml";

        private String sqlmapFile = DEFAULT_SQLMAP_FILE;

        private Unmarshaller unmarshaller;

        public void setSqlmapFile(String sqlmapFile) {
            this.sqlmapFile = sqlmapFile;
        }

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            Source xmlSource = new StreamSource(UserDao.class.getResourceAsStream(sqlmapFile));

            try {
                Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(xmlSource);

                for (SqlType sql : sqlmap.getSql()) {
                    sqlRegistry.registry(sql.getKey(), sql.getValue());
                }
            } catch (IOException e) {
                new IllegalArgumentException(sqlmapFile + "을 가져올 수 없습나다.");
            }
        }
    }
}
