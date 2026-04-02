package vn.be.platform_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ResponseStatusException;
import vn.be.platform_service.dto.WhitelistRequest;
import vn.be.platform_service.entity.Whitelist;
import vn.be.platform_service.repositories.WhitelistRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhitelistService {


    //Access Modified
    private static final String REDIS_KEY = "security::whitelist::pattern";
    private final RedisTemplate<String, Object> redisTemplate;

    private final WhitelistRepository whitelistRepository;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    //reload whitelist từ db vào redis
    //gọi tự động khi thêm xóa url qua api
    // tự động chạy mỗi 5p để đồng bộ nếu có thay đổi trực tiếp trong database
    @Scheduled(fixedRate = 300000)
    public void refreshCache() {
        List<String> listPattern = whitelistRepository.findByEnabledTrue().stream().map(Whitelist::getPattern).collect(Collectors.toList());

        //xóa key cũ đi và đẩy lại vào redis set
        redisTemplate.delete(REDIS_KEY);
        if (!listPattern.isEmpty()) {
            redisTemplate.opsForValue().set(REDIS_KEY, listPattern.toArray());
        }

        log.info("Đã reload whitelist", listPattern.size(), listPattern);
    }

    //Kiểm tra xem url api có nằm trong whitelist hay không ( đọc từ redis )
    //ôn về redis
    //set . map , hashmap, hashset
    public boolean isWhitelisted(String path) {
        Set<Object> patterns = redisTemplate.opsForSet().members(REDIS_KEY);

        if (patterns == null || patterns.isEmpty()) {
            return false;
        }

        return patterns.stream().map(Object::toString).anyMatch(pattern -> antPathMatcher.match(pattern, path));

    }

    public List<String> getWhitelistPattern() {
        Set<Object> patterns = redisTemplate.opsForSet().members(REDIS_KEY);

        return patterns.stream().map(Object::toString).collect(Collectors.toList());
    }

    //lấy tất cả list whitelist url từ database
    public List<Whitelist> getAllWhitelist() {
        return whitelistRepository.findAll();
    }

    public Whitelist toggleWhitelist(Long id) {
        Whitelist url = whitelistRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        url.setEnabled(!url.getEnabled());
        Whitelist saved = whitelistRepository.save(url);

        refreshCache();
        return saved;
    }

    public void removeWhitelist(Long id) {
        whitelistRepository.deleteById(id);
        refreshCache();
    }

    public List<Whitelist> bulkAddWhitelist(List<String> patterns, String description) {
        List<Whitelist> listWhitelistInDB = whitelistRepository.findAll();
        //A,B,C,D

        Set<String> existingPatterns = listWhitelistInDB.stream().map(Whitelist::getPattern).collect(Collectors.toSet());
        // A_url , B_url, C_url , D_url

        List<Whitelist> toSave = patterns.stream().filter(p -> p != null && !p.isBlank()).filter(p -> !existingPatterns.contains(p)).map(p -> {
            Whitelist newWhitelist = new Whitelist();
            newWhitelist.setPattern(p);
            newWhitelist.setDescription(description);
            newWhitelist.setEnabled(true);

            return newWhitelist;
        }).collect(Collectors.toList());

        List<Whitelist> saved = whitelistRepository.saveAll(toSave);

        if (!saved.isEmpty()) {
            refreshCache();
        }

        return saved;
    }

    public Whitelist createWhitelist(WhitelistRequest whitelist) {
        Whitelist whiteListUrl = new Whitelist();
        whiteListUrl.setPattern(whitelist.getPattern());
        whiteListUrl.setDescription(whitelist.getDescription());
        whiteListUrl.setEnabled(true);

        Whitelist saved = whitelistRepository.save(whiteListUrl);

        return saved;
    }
}

// nghiên cứu