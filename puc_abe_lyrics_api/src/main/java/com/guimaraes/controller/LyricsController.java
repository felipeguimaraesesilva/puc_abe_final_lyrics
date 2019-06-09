package com.guimaraes.controller;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guimaraes.constant.AppConstants;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class LyricsController {

	private static final String RESOURCE_NAME = "/lyric";
	private static final String CONTEXT_RESOURCE = "/" + AppConstants.VERSION_V1 + RESOURCE_NAME;

	@HystrixCommand(fallbackMethod = "reliable")
	@GetMapping(CONTEXT_RESOURCE)
	public ResponseEntity<Lyrics> getLyrics(@RequestParam("trackID") int trackId) {

		MusixMatch musixMatch = new MusixMatch(AppConstants.MUSICX_MATCH_API_KEY);
		try {
			return new ResponseEntity<Lyrics>(musixMatch.getLyrics(trackId), HttpStatus.OK);
		} catch (MusixMatchException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(new Lyrics(), HttpStatus.NO_CONTENT);
	}

	public ResponseEntity<Lyrics> reliable(int trackId) {
		return new ResponseEntity<>(new Lyrics(), HttpStatus.NO_CONTENT);
	}

}
