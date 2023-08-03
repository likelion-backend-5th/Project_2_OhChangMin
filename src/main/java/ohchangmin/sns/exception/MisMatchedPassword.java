package ohchangmin.sns.exception;

public class MisMatchedPassword extends CustomException {

    private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public MisMatchedPassword() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
