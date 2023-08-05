package ohchangmin.sns.exception;

public class NotFoundComment extends CustomException {

    private static final String MESSAGE = "존재하지 않는 댓글입니다.";

    public NotFoundComment() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
