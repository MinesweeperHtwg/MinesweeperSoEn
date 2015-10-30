package minesweeper.model;

public interface ICell {
    int getMines();

    boolean isOpened();
    
    boolean isFlag();
    
    boolean isMine();
}
