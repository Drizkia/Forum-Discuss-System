package sistemforumdiskusi_19.model;

/**
 * Interface Segregation Principle (ISP):
 * Interface yang spesifik untuk entitas yang dapat ditampilkan sebagai teks.
 * Klien yang hanya butuh fitur tampilan tidak dipaksa untuk bergantung pada method lain.
 */
public interface Displayable {
    String getDisplayText();
}
