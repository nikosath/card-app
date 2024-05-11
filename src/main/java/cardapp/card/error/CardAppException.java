package cardapp.card.error;

import lombok.Getter;

@Getter
public class CardAppException extends RuntimeException {

    private final ErrorEnum errorEnum;
    private final String message;

    public static CardAppException with(ErrorEnum errorEnum, String message) {
        return new CardAppException(errorEnum, message);
    }

    public CardAppException(ErrorEnum errorEnum, String message) {
        this.errorEnum = errorEnum;
        this.message = message;
    }

    public static CardAppException notFound(String msg) {
        return with(ErrorEnum.NOT_FOUND, msg);
    }
}
