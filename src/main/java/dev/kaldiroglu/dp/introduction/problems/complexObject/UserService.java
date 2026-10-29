package dev.kaldiroglu.dp.introduction.problems.complexObject;

import dev.kaldiroglu.dp.introduction.problems.complexObject.user.*;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    public User createUser(NewUserRequest newUserRequest, String username, String userEntityId) {
        UserFactory userFactory = new UserFactory();
        List<UserRole> userRoleList = new ArrayList<>();
        List<UserCategory> userCategories = new ArrayList<>();
        List<Configuration> configurations = new ArrayList<>();
        User user = userFactory.createUserFromRequest(newUserRequest, username, userEntityId,
                newUserRequest.getRemoteAddr(), newUserRequest.getHeader("User-Agent"));

        RoleFactory roleFactory = new RoleFactory();
        if (newUserRequest.getRoleREntityIds()!= null
                && newUserRequest.getRoleREntityIds().size() > 0) {
            newUserRequest.getRoleREntityIds().stream().forEach(roleEntityId -> {
                UserRole userRole = roleFactory.createUserRoleFromRequest(roleEntityId, username, userEntityId,
                        newUserRequest.getRemoteAddr(), newUserRequest.getHeader("User-Agent"));
                userRole.setUser(user);
                userRoleList.add(userRole);
            });
            user.setRoles(userRoleList);
        }

        UserCategoryFactory userCategoryFactory = new UserCategoryFactory();
        if (newUserRequest.getCategoryEntityIds() != null
                && newUserRequest.getCategoryEntityIds().size() > 0) {
            newUserRequest.getCategoryEntityIds().stream().forEach(disciplineResourceId -> {
                UserCategory userCategory = userCategoryFactory.createCatagoryFromRequest(disciplineResourceId, username, userEntityId,
                        newUserRequest.getRemoteAddr(), newUserRequest.getHeader("User-Agent"));
                userCategory.setUser(user);
                userCategories.add(userCategory);
            });
            user.setCategories(userCategories);
        }

        ConfigurationFactory configurationFactory = new ConfigurationFactory();

        if (newUserRequest.getConfigurationEntityIds() != null
                && newUserRequest.getConfigurationEntityIds().size() > 0) {
            newUserRequest.getConfigurationEntityIds().stream().forEach(configurationEntityId -> {
                Configuration configuration = configurationFactory.createConfigurationFromRequest(configurationEntityId, username, userEntityId,
                        newUserRequest.getRemoteAddr(), newUserRequest.getHeader("User-Agent"));
                configuration.setUser(user);
                configurations.add(configuration);
            });
            user.setConfigurations(configurations);
        }

        return user;
    }
}
