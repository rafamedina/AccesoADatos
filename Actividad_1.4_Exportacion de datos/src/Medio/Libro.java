package Medio;

public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private String categoria;
    private int anoPublicacion;
    private int numPaginas;
    private boolean disponible;
    private int prestamos;

    // Constructor
    public Libro(String isbn, String titulo, String autor, String categoria,
                 int añoPublicacion, int numPaginas, boolean disponible, int prestamos) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.anoPublicacion = añoPublicacion;
        this.numPaginas = numPaginas;
        this.disponible = disponible;
        this.prestamos = prestamos;
    }

    // Getters y Setters
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getAñoPublicacion() { return anoPublicacion; }
    public void setAñoPublicacion(int anoPublicacion) { this.anoPublicacion = anoPublicacion; }

    public int getNumPaginas() { return numPaginas; }
    public void setNumPaginas(int numPaginas) { this.numPaginas = numPaginas; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public int getPrestamos() { return prestamos; }
    public void setPrestamos(int prestamos) { this.prestamos = prestamos; }
}

