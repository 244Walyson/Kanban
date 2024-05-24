package com.waly.kanban.services.validation;

import com.waly.kanban.dto.UserUpdateDTO;
import com.waly.kanban.entities.User;
import com.waly.kanban.exceptions.FieldMessage;
import com.waly.kanban.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    private static final Logger log = LoggerFactory.getLogger(UserUpdateValidator.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        @SuppressWarnings("unchecked")
        var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String idString = uriVars.get("id");
        Long userId = Long.parseLong(idString);

        List<FieldMessage> list = new ArrayList<>();

        User user = userRepository.findByEmail(dto.getEmail()).orElse(null);
        if(user != null && userId != user.getId()){
            list.add(new FieldMessage("Email", "Email ja cadastrado"));
        }
        if (user != null
                && userId != user.getId()
                && !user.getNickname().equals(dto.getNickname())
                && userRepository.existsByNickname(dto.getNickname())) {
            list.add(new FieldMessage("Nickname", "Nickname ja cadastrado"));
        }
        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}