package br.com.anthonycruz.planner.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.anthonycruz.planner.dtos.LinkDTO;
import br.com.anthonycruz.planner.mocks.MockLink;
import br.com.anthonycruz.planner.mocks.MockTrip;
import br.com.anthonycruz.planner.models.Link;
import br.com.anthonycruz.planner.models.Trip;
import br.com.anthonycruz.planner.repositories.LinkRepository;
import br.com.anthonycruz.planner.requests.LinkRequest;

public class LinkServiceTest {
    @Mock
    LinkRepository repository;

    @InjectMocks
    LinkService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterLink() {
        UUID testTripId = UUID.fromString("33f609fc-004b-4fc2-a635-71d2eae72060");
        Trip trip = MockTrip.mockEntity(testTripId);

        Link link = MockLink.mockEntity();
        when(repository.save(any(Link.class))).thenReturn(link);

        LinkRequest request = MockLink.mockRequest();
        Link savedLink = service.registerLink(request, trip);

        assertNotNull(savedLink.getId());
        assertEquals(link.getTitle(), savedLink.getTitle());
        assertEquals(link.getUrl(), savedLink.getUrl());
    }

    @Test
    public void testGetAllLinksFromTrip() {
        UUID testTripId = UUID.fromString("33f609fc-004b-4fc2-a635-71d2eae72060");
        when(repository.findByTripId(any(UUID.class))).thenReturn(MockLink.mockEntities(5));
        List<LinkDTO> links = service.getAllLinksFromTrip(testTripId);

        assertNotNull(links);
        assertEquals(links.size(), 5);
        assertNotNull(links.getFirst().id());
        assertEquals(links.getFirst().title(), "Google 0");
        assertEquals(links.getFirst().url(), "https://www.google.com.br/0");
    }
}
