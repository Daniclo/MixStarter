import org.daniclo.mixstarter.dao.AlbumDAO;
import org.daniclo.mixstarter.dao.AlbumDAOImpl;
import org.daniclo.mixstarter.model.Album;

public class DataModelTests {
    public static void main(String[] args) {
        TestDataAccess();
    }

    private static void TestDataAccess() {
        AlbumDAO albumDAO = new AlbumDAOImpl(Album.class);
        System.out.println(albumDAO.getAlbums());
    }
}
