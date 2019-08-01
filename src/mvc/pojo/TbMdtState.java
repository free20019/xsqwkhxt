package mvc.pojo;

public class TbMdtState {
    private String msId;

    private String msName;

    private String note;

    public String getMsId() {
        return msId;
    }

    public void setMsId(String msId) {
        this.msId = msId == null ? null : msId.trim();
    }

    public String getMsName() {
        return msName;
    }

    public void setMsName(String msName) {
        this.msName = msName == null ? null : msName.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }
}