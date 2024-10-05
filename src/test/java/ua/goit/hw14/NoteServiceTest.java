package ua.goit.hw14;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListAll() {
        Note note1 = new Note();
        note1.setId(1L);
        note1.setContent("Note 1");

        Note note2 = new Note();
        note2.setId(2L);
        note2.setContent("Note 2");

        when(noteRepository.findAll()).thenReturn(Arrays.asList(note1, note2));

        List<Note> notes = noteService.listAll();

        assertEquals(2, notes.size());
        assertEquals("Note 1", notes.get(0).getContent());
        assertEquals("Note 2", notes.get(1).getContent());
        verify(noteRepository).findAll();
    }

    @Test
    void testAdd() {
        Note note = new Note();
        note.setContent("New Note");

        when(noteRepository.save(note)).thenReturn(note);

        Note addedNote = noteService.add(note);

        assertNotNull(addedNote);
        assertEquals("New Note", addedNote.getContent());
        verify(noteRepository).save(note);
    }

    @Test
    void testDeleteById_ExistingNote() {
        long id = 1L;

        noteService.deleteById(id);

        verify(noteRepository).deleteById(id);
    }

    @Test
    void testUpdate_ExistingNote() {
        Note note = new Note();
        note.setId(1L);
        note.setContent("Updated Note");

        when(noteRepository.existsById(1L)).thenReturn(true);
        when(noteRepository.save(note)).thenReturn(note);

        noteService.update(note);

        verify(noteRepository).save(note);
    }

    @Test
    void testUpdate_NoteNotFound() {
        Note note = new Note();
        note.setId(999L);

        when(noteRepository.existsById(999L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            noteService.update(note);
        });

        verify(noteRepository, never()).save(note);
    }

    @Test
    void testGetById_ExistingNote() {
        Note note = new Note();
        note.setId(1L);
        note.setContent("Found Note");

        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        Note foundNote = noteService.getById(1L);

        assertEquals(note, foundNote);
        verify(noteRepository).findById(1L);
    }

    @Test
    void testGetById_NoteNotFound() {
        long id = 999L;

        when(noteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            noteService.getById(id);
        });
    }
}
