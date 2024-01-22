import com.sparta.entity.Memo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PersistanceTest {
    EntityManagerFactory emf;
    EntityManager em;

    @BeforeEach
    void setUp() {
        this.emf = Persistence.createEntityManagerFactory("memo");
        this.em = this.emf.createEntityManager();
    }
    //
    @Test
    @DisplayName("1차 캐시 : Entity 저장")
    void test1() {
        EntityTransaction et = em.getTransaction();
        et.begin();
        try {
            Memo memo = new Memo();
            memo.setId(1L);
            memo.setUsername("Robbie");
            memo.setContents("1차 캐시 Entity 저장");
            em.persist(memo);
            et.commit();
        } catch (Exception ex) {
            System.out.println("중복 문제 일수 있음");
            ex.printStackTrace();
            et.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
    //메모 조회
    @Test
    @DisplayName("Entity 조회 : 캐시 저장소에 해당하는 Id가 존재하지 않은 경우")
    void test2() {
        try {
            Memo memo = em.find(Memo.class, 1);
            System.out.println("memo.getId() = " + memo.getId());
            System.out.println("memo.getUsername() = " + memo.getUsername());
            System.out.println("memo.getContents() = " + memo.getContents());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }

}
