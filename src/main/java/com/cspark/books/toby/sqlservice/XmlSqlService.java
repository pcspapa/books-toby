package com.cspark.books.toby.sqlservice;

import com.cspark.books.toby.dao.UserDao;
import com.cspark.books.toby.sqlservice.jaxb.SqlType;
import com.cspark.books.toby.sqlservice.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cspark on 2015. 12. 28..
 */
public class XmlSqlService implements SqlService {

    private Map<String, String> sqlMap = new HashMap<>();

    public XmlSqlService() {
        String contextPath = Sqlmap.class.getPackage().getName();

        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = UserDao.class.getResourceAsStream("sqlmap.xml");
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);

            for (SqlType sql : sqlmap.getSql()) {
                sqlMap.put(sql.getKey(), sql.getValue());
                System.out.println(sql.getKey() + ", " + sql.getValue());
            }

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = sqlMap.get(key);
        if (sql == null)
            throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다");
        else
            return sql;
    }
}
