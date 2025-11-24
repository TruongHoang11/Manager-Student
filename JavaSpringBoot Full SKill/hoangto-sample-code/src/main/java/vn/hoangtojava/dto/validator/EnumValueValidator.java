package vn.hoangtojava.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Stream;

public class EnumValueValidator implements ConstraintValidator<EnumValue, CharSequence> {
    private List acceptedValues;

    @Override
    public void initialize(EnumValue enumValue) {
        acceptedValues = Stream.of(enumValue.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
    }
    //    (Enum::name) <=> e -> e.name() tuc la moi phan tu Enum goi ham .name() -> lay ra chuoi dang String
//    @Override
//    public void initialize(EnumValue enumValue) {
//        // Lấy toàn bộ giá trị trong Enum
//        Enum<?>[] constants = enumValue.enumClass().getEnumConstants();
//
//        // Tạo danh sách trống để lưu tên các Enum
//        List<String> list = new ArrayList<>();
//
//        // Duyệt từng giá trị trong Enum và thêm vào list
//        for (Enum<?> e : constants) {
//            list.add(e.name());
//        }
//
//        // Gán danh sách đó cho biến acceptedValues
//        acceptedValues = list;
//    }


    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return acceptedValues.contains(value.toString().toUpperCase());
    }
}