package com.example.myhome.services;

import com.example.myhome.home.exception.NotFoundException;
import com.example.myhome.home.model.Message;
import com.example.myhome.home.model.filter.FilterForm;
import com.example.myhome.home.repository.MessageRepository;
import com.example.myhome.home.service.impl.MessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Service
@RequiredArgsConstructor
public class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_ValidId_ReturnsMessage() {
        // Arrange
        Long messageId = 1L;
        Message message = new Message();
        message.setId(messageId);
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // Act
        Message result = messageService.findById(messageId);

        // Assert
        assertEquals(message, result);
    }

    @Test
    public void testFindById_InvalidId_ThrowsNotFoundException() {

        Long invalidMessageId = 999L;
        when(messageRepository.findById(invalidMessageId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> messageService.findById(invalidMessageId));
    }

    @Test
    public void testSave_ReturnsSavedMessage() {

        Message messageToSave = new Message();
        messageToSave.setText("Test message");
        when(messageRepository.save(any(Message.class))).thenReturn(messageToSave);

        Message result = messageService.save(messageToSave);

        assertEquals(messageToSave, result);
    }

    @Test
    public void testFindAllBySpecification_ReturnsPageOfMessages() {
        FilterForm filterForm = new FilterForm();
        Integer page = 1;
        Integer size = 10;
        Long ownerId = 1L;

        Page<Message> mockedPage = new PageImpl<>(List.of(new Message(), new Message()));
        when(messageRepository.findByFilters(any(), anyLong(), any(Pageable.class))).thenReturn(mockedPage);

        Page<Message> result = messageService.findAllBySpecification(filterForm, page, size, ownerId);

        assertEquals(mockedPage, result);
    }
}
