package br.com.anthonycruz.planner.mocks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.anthonycruz.planner.models.Link;
import br.com.anthonycruz.planner.requests.LinkRequest;

public class MockLink {
    public static List<Link> mockEntities(int qty) {
        List<Link> links = new ArrayList<>();
        for (int i = 0; i < qty; i++) {
            Link link = new Link();
            link.setId(UUID.randomUUID());
            link.setTitle("Google " + i);
            link.setUrl("https://www.google.com.br/" + i);
            links.add(link);
        }
        return links;
    }

    public static LinkRequest mockRequest() {
        return new LinkRequest("Google", "https://www.google.com.br/");
    }

    public static Link mockEntity() {
        Link link = new Link();
        link.setId(UUID.randomUUID());
        link.setTitle("Google");
        link.setUrl("https://www.google.com.br/");
        return link;
    }
}
