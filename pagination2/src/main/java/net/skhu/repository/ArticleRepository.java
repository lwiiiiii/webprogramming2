package net.skhu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.Article;
import net.skhu.model.Option;
import net.skhu.model.Pagination;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    Option[] searchBy = { new Option(0, "검색 없음"), new Option(1, "글쓴이"),
                          new Option(2, "제목"), new Option(3, "본문")  };
    Option[] orderBy = { new Option(0, "최근 글"), new Option(1, "오래된 글"),
                         new Option(2, "글쓴이") };

    Sort[] sort = { new Sort(Sort.Direction.DESC, "id"),
                    new Sort(Sort.Direction.ASC, "id"),
                    new Sort(Sort.Direction.ASC, "user_name") };

    public default List<Article> findAll(Pagination pagination) {
        Pageable pageable = 
                  PageRequest.of(pagination.getPg() - 1, pagination.getSz(), sort[pagination.getOb()]);

        Page<Article> page;
        int boardId = pagination.getBd();
        String searchText = pagination.getSt();
        switch (pagination.getSb()) {
        case 1: page = this.findByBoardIdAndUserName(boardId, searchText, pageable); break;
        case 2: page = this.findByBoardIdAndTitleContains(boardId, searchText, pageable); break;
        case 3: page = this.findByBoardIdAndBodyContains(boardId, searchText, pageable); break;
        default: page = this.findByBoardId(boardId, pageable); break;
        }
        pagination.setRecordCount((int)page.getTotalElements());
        return page.getContent();
    }

    public Page<Article> findByBoardId(int boardId, Pageable pageable);
    // 검색할 문자열과 사용자 이름이 전체 일치해야함
    public Page<Article> findByBoardIdAndUserName(int boardId, String name, Pageable pageable);
    // 검색할 문자열이 제목에 포함되어 있는지만 비교
    public Page<Article> findByBoardIdAndTitleContains(int boardId, String title, Pageable pageable);
    public Page<Article> findByBoardIdAndBodyContains(int boardId, String body, Pageable pageable);
}
