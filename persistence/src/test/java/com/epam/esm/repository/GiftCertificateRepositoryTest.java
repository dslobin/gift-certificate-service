package com.epam.esm.dao.impl;

/*@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JpaContextTest.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional*/
class GiftCertificateDaoImplTest {
/*    @Autowired
    private GiftCertificateDao certificateDao;

    @Test
    @Sql(scripts = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleCertificates_whenFindAll_thenGetCorrectCount() {
        int page = 1;
        int size = 5;
        List<GiftCertificate> actualCertificates = certificateDao.findAll(page, size);

        int expectedCertificatesCount = 5;
        assertEquals(expectedCertificatesCount, actualCertificates.size());
    }

    @Test
    @Sql(scripts = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenMultipleCertificates_whenFindAllWithParameters_thenGetCorrectCount() {
        String paramName = "курс";
        int page = 1;
        int size = 5;
        CertificateSearchCriteria criteria = new CertificateSearchCriteria(null, paramName, null, null, null);
        List<GiftCertificate> actualCertificates = certificateDao.findAll(criteria, page, size);

        int expectedCertificatesCount = 2;
        assertEquals(expectedCertificatesCount, actualCertificates.size());
    }

    @Test
    @Sql(scripts = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenCertificateId_whenFindById_thenGetCorrectGiftCertificate() {
        long requiredCertificateId = 1;
        Optional<GiftCertificate> savedCertificate = certificateDao.findById(requiredCertificateId);

        assertTrue(savedCertificate.isPresent());

        long actualCertificateId = savedCertificate.get().getId();
        assertEquals(requiredCertificateId, actualCertificateId);
    }

    @Test
    void givenGiftCertificate_whenSave_thenGetCorrectGiftCertificateId() {
        GiftCertificate certificate = createCertificate();
        Set<Tag> certificateTags = certificate.getTags();

        GiftCertificate savedCertificate = certificateDao.save(certificate);
        Set<Tag> savedCertificateTags = savedCertificate.getTags();

        assertEquals(certificateTags.size(), savedCertificateTags.size());
        long expectedTagId = 1;
        assertEquals(expectedTagId, savedCertificate.getId());
    }

    private GiftCertificate createCertificate() {
        String name = "Diving Lessons";
        String description = "Gift certificate entitles you to attend 3 diving lessons from a professional instructor";
        BigDecimal price = BigDecimal.valueOf(135.15);
        ZonedDateTime createDate = ZonedDateTime.now();
        Duration duration = Duration.ofDays(31);
        Set<Tag> tags = createTags();

        return new GiftCertificate(0, name, description, price, createDate, createDate, duration, tags, true);
    }

    private Set<Tag> createTags() {
        Tag tagActiveRest = new Tag(0, "active_rest", null);
        Tag tagSport = new Tag(0, "sport", null);
        return Stream.of(tagActiveRest, tagSport)
                .collect(Collectors.toSet());
    }

    @Test
    @Sql(scripts = {"/test-one-certificate-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenGiftCertificateId_whenUpdateCertificate_thenGetCorrectGiftCertificate() {
        long certificateId = 1;
        Optional<GiftCertificate> certificateOptional = certificateDao.findById(certificateId);

        assertTrue(certificateOptional.isPresent());
        GiftCertificate certificate = certificateOptional.get();
        BigDecimal newPrice = BigDecimal.valueOf(100);
        ZonedDateTime lastUpdateDate = ZonedDateTime.now();
        certificate.setPrice(newPrice);
        certificate.setLastUpdateDate(lastUpdateDate);

        certificateDao.update(certificate);
        //assertFalse(Objects.isNull(certificate));
        assertEquals(newPrice, certificate.getPrice());
        assertEquals(lastUpdateDate, certificate.getLastUpdateDate());
    }

    @Test
    @Sql(scripts = {"/test-one-certificate-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void givenGiftCertificateId_whenDelete_thenGetOk() {
        long requiredCertificateId = 1;
        certificateDao.deleteById(requiredCertificateId);
    }*/
}
