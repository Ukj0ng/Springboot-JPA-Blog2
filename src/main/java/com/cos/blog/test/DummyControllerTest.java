package com.cos.blog.test;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// html파일이 아니라 data를 리턴해주는 컨트롤러
@RestController
public class DummyControllerTest {

    @Autowired // 의존성 주입(DI)
    private UserRepository userRepository;


    @DeleteMapping("/dummy/user/{id}")
    public String delete(@PathVariable int id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return "삭제되었습니다. id: " + id;
        }).orElseThrow(() -> new IllegalArgumentException("삭제에 실패하였습니다. 해당 id는 DB에 없습니다."));
//        try {
//            userRepository.deleteById(id);
//        } catch (EmptyResultDataAccessException e) {
//            System.out.println("삭제실패");
//            return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
//        }
//        userRepository.deleteById(id);
//
//        return "삭제되었습니다. id: " + id;
    }

    // save함수는 id를 전달하지 않으면 insert를 해주고
    // save함수는 id를 전달하면 해당 id에 대한 데이터가 있으면 update를 해주고
    // save함수는 id를 전달하면 해당 id에 대한 데이터가 없으면 insert를 함
    // email, password
    @Transactional // 함수 종료 시에 자동으로 commit 됨
    @PutMapping("/dummy/user/{id}")
    // @RequestBody: json 데이터를 요청 -> Java Object(MessageConverter의 Jackson라이브러리로 변환해서 받아줌.
    public User updateUser(@PathVariable int id, @RequestBody User requestUser) {
        System.out.println("id: " + id);
        System.out.println("password: " + requestUser.getPassword());
        System.out.println("email: " + requestUser.getEmail());

        User user = userRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("수정에 실패하였습니다.");
        });

        user.setPassword(requestUser.getPassword());
        user.setEmail(requestUser.getEmail());
//        userRepository.save(user);

        // 더티 체킹
        return user;
    }

    // http://localhost:8000/blog/dummy/user
    @GetMapping("/dummy/users")
    public List<User> list() {
        return userRepository.findAll();
    }

    // 한페이지당 2건에 데이터를 리턴받아 볼 예정
    @GetMapping("/dummy/user")
    public List<User> pageList(@PageableDefault(size = 2, sort = "id", direction = Direction.DESC) Pageable pageable) {
        Page<User> pagingUser = userRepository.findAll(pageable);

        List<User> users = pagingUser.getContent();
        return users;
    }
    // {id} 주소로 파라미터를 전달 받을 수 있음
    // http://localhost:8000/blog/dummy/user/3
    @GetMapping("/dummy/user/{id}")
    public User detail(@PathVariable int id) {
        // user/4을 찾으면 내가 db에서 못찾아오게 되면 user가 null이 될 것 아냐?
        // 그럼 return null이 리턴되니까 프로그램에 문제가 생겨
        // Optional로 너의 User를 감싸서 가져올테니 null인지 아닌지 판단해
        // 람다식 userRepository.findById().orElseThrow(() -> {
        //            return new IllegalArgumentException("해당 사용자는 없습니다.");
        //        })
        User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("해당 유저는 없습니다. id: " + id);
            }
        });
        // 요청: 웹브라우저
        // user 객체 = 자바 오브젝트
        // 리턴할 user 객체를 웹브라우저가 이해할 수 있는 데이터로 변환해야함 -> json
        // 스프링부트 = MessageConverter라는 애가 응답시 자동으로 작동
        // 만약 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출해서
        // user 오브젝트를 json으로 변환해서 브라우저에게 전송
        return user;
    }

    // http://localhost:8000/blog/dummy/join (요청)
    // http의 body에 username, password, email 데이터를 가지고 (요청)
    @PostMapping("/dummy/join")
    public String join(User user) { // key=value(약속된 규칙)
        System.out.println("id: " + user.getId());
        System.out.println("username: " + user.getUsername());
        System.out.println("password: " + user.getPassword());
        System.out.println("email: " + user.getEmail());
        System.out.println("role: " + user.getRole());
        System.out.println("createDate: " + user.getCreateDate());

        user.setRole(RoleType.USER);
        userRepository.save(user);
        return "회원가입이 완료되었습니다.";
    }
}
