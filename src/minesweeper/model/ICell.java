package minesweeper.model;

public interface ICell {
    int getMines();

    void setMines(int mines);
    
    boolean isOpened();
    
    void setOpened(boolean opened);
    
    boolean isFlag();
    
    void setFlag(boolean flag);
    
    boolean containsMine();
    
    void setContainsMine(boolean containsMine);
}
