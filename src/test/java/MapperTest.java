import static org.junit.Assert.assertEquals;

import com.ramazan.myapp.MyAppTokenMapper;
import com.ramazan.myapp.MyAppUser;
import com.ramazan.myapp.MyAppUserMapper;
import com.ramazan.myapp.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
public class MapperTest {

    @Test
    public void checkCreate() {
        SqlSession sqlSession = MyBatisUtil.getSession();
        MyAppUserMapper myAppUserMapper = sqlSession.getMapper(MyAppUserMapper.class);
        MyAppUser myAppUser = new MyAppUser("Denef", "denef@mail.com", "dhf4834yf87g8374f7843gffg");
        myAppUserMapper.createUser(myAppUser);
        sqlSession.commit();

        assertEquals(myAppUser.getId(), myAppUserMapper.getUserById(myAppUser.getId()).getId());

        myAppUserMapper.deleteUser(myAppUser);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void checkUpdate() {
        SqlSession sqlSession = MyBatisUtil.getSession();
        MyAppUserMapper myAppUserMapper = sqlSession.getMapper(MyAppUserMapper.class);
        MyAppUser myAppUser = new MyAppUser("Denef", "denef@mail.com", "dhf4834yf87g8374f7843gffg");
        myAppUserMapper.createUser(myAppUser);
        sqlSession.commit();

        assertEquals(myAppUser.getId(), myAppUserMapper.getUserById(myAppUser.getId()).getId());

        myAppUser.setName("Clark");
        myAppUserMapper.updateUser(myAppUser);
        sqlSession.commit();

        assertEquals("Clark", myAppUserMapper.getUserById(myAppUser.getId()).getName());

        myAppUserMapper.deleteUser(myAppUser);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void checkDelete() {
        SqlSession sqlSession = MyBatisUtil.getSession();
        MyAppUserMapper myAppUserMapper = sqlSession.getMapper(MyAppUserMapper.class);
        MyAppUser myAppUser = new MyAppUser("Denef", "denef@mail.com", "dhf4834yf87g8374f7843gffg");
        myAppUserMapper.createUser(myAppUser);
        sqlSession.commit();

        assertEquals(myAppUser.getId(), myAppUserMapper.getUserById(myAppUser.getId()).getId());

        myAppUserMapper.deleteUser(myAppUser);
        sqlSession.commit();

        assertEquals(null, myAppUserMapper.getUserById(myAppUser.getId()));
        sqlSession.close();
    }

    @Test
    public void checkLoadUserByUserName(){
        SqlSession sqlSession = MyBatisUtil.getSession();
        MyAppUserMapper myAppUserMapper = sqlSession.getMapper(MyAppUserMapper.class);
        MyAppUser myAppUser = new MyAppUser("Denef", "denef@mail.com", "dhf4834yf87g8374f7843gffg");
        myAppUserMapper.createUser(myAppUser);
        sqlSession.commit();

        assertEquals("Denef", myAppUserMapper.getUserByUserName("Denef").getName());

        myAppUserMapper.deleteUser(myAppUser);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void checkTokenAdd(){
        SqlSession sqlSession = MyBatisUtil.getSession();
        String token = "tine";
        MyAppTokenMapper myAppTokenMapper = sqlSession.getMapper(MyAppTokenMapper.class);
        myAppTokenMapper.addToken(token);
        sqlSession.commit();
        assertEquals(1, myAppTokenMapper.countTokens(token));
        myAppTokenMapper.deleteToken(token);
        sqlSession.commit();
        sqlSession.close();
    }


}
