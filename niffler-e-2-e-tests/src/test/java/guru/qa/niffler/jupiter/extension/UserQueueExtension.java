package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

import static guru.qa.niffler.model.UserJson.simpleUser;

// Любой тест проходит через него
public class UserQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UserQueueExtension.class);

    private static final Map<User.UserType, Queue<UserJson>> USERS = new ConcurrentHashMap<>();

    static {
        USERS.put(User.UserType.WITH_FRIEND, new ConcurrentLinkedQueue<>(
                List.of(
                        simpleUser("oleg", "ZaharenkoOP"),
                        simpleUser("duck", "ZaharenkoOP")
                ))
        );
        USERS.put(User.UserType.INVITE_SENT, new ConcurrentLinkedQueue<>(
                List.of(
                        simpleUser("dog", "ZaharenkoOP"),
                        simpleUser("cat", "ZaharenkoOP")
                ))
        );
        USERS.put(User.UserType.INVITE_RECEIVED, new ConcurrentLinkedQueue<>(
                List.of(
                        simpleUser("worm", "ZaharenkoOP"),
                        simpleUser("deer", "ZaharenkoOP")
                ))
        );
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        List<User> desiredUsersForTest = new ArrayList<>();

        Stream.concat(
                Stream.of(context.getRequiredTestMethod()),
                Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(BeforeEach.class))
        ).forEach(method -> {
            desiredUsersForTest.addAll(
                    Arrays.stream(method.getParameters())
                            .filter(p -> AnnotationSupport.isAnnotated(p, User.class))
                            .map(p -> p.getAnnotation(User.class))
                            .toList()
            );
        });

        Map<User.UserType, List<UserJson>> usersForTest = new HashMap<>();
        for (User desiredUser : desiredUsersForTest) {
            User.UserType userType = desiredUser.value();
            Queue<UserJson> userQueue = USERS.get(userType);
            if (userQueue != null) {
                List<UserJson> usersForType = usersForTest.computeIfAbsent(userType, k -> new ArrayList<>());
                UserJson userForTest = null;
                while (userForTest == null) {
                    userForTest = userQueue.poll();
                }
                usersForType.add(userForTest);
            }
        }
        Allure.getLifecycle().updateTestCase(testCase -> {
            testCase.setStart(new Date().getTime());
        });
        context.getStore(NAMESPACE).put(context.getUniqueId(), usersForTest);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<User.UserType, List<UserJson>> usersAfterTest = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        if (usersAfterTest != null) {
            for (Map.Entry<User.UserType, List<UserJson>> user : usersAfterTest.entrySet()) {
                USERS.get(user.getKey()).addAll(user.getValue());
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(UserJson.class) && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

//    @Override
//    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
//        Optional<User> annotation = AnnotationSupport.findAnnotation(parameterContext.getParameter(), User.class);
//        User.UserType userType = annotation.get().value();
//        Map<User.UserType, List<UserJson>> users = extensionContext.getStore(NAMESPACE).
//                get(extensionContext.getUniqueId(), Map.class);
//        return users.get(userType).remove(0);
//    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        User.UserType userType = parameterContext.getParameter().getAnnotation(User.class).value();
        Map<User.UserType, List<UserJson>> users = extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class);

        List<UserJson> userJsonList = users.get(userType);
        UserJson user = null;

        if (userJsonList != null && !userJsonList.isEmpty()) {
            user = userJsonList.getFirst();
            if (userJsonList.size() > 1) {
                userJsonList.remove(user);
                userJsonList.add(user);
            }
        }

        return user;
    }
}
