package jreader.services;

import java.util.List;

import jreader.dto.ArchiveDto;
import jreader.dto.ArchivedPostDto;

public interface ArchiveService {

    ArchiveDto createArchive(String username, String title);

    void deleteArchive(String username, Long archiveId);

    void moveUp(String username, Long archiveId);

    void moveDown(String username, Long archiveId);

    List<ArchiveDto> list(String username);

    void entitle(String username, Long archiveId, String title);

    void archive(String username, Long groupId, Long subscriptionId, Long postId, Long archiveId);

    List<ArchivedPostDto> listPosts(ArchivedPostFilter filter);

    void deletePost(String username, Long archiveId, Long postId);

}
