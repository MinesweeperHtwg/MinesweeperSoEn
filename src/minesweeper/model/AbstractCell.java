package minesweeper.model;

public class AbstractCell implements ICell {
    private int mines;
    private boolean opened;
    private boolean flag;
    private boolean containsMine;
    
    public int getMines() {
        return mines;
    }
    public void setMines(int mines) {
        this.mines = mines;
    }
    public boolean isOpened() {
        return opened;
    }
    public void setOpened(boolean opened) {
        this.opened = opened;
    }
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public boolean containsMine() {
        return containsMine;
    }
    public void setContainsMine(boolean containsMine) {
        this.containsMine = containsMine;
    }
}
