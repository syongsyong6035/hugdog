USE dogCancer;
-- 📘 1. Board (게시판 - ID 자동 증가 가정)
INSERT INTO board (type, created_at, modified_at, activated)
VALUES
    ('INFO_SHARE', NOW(), NOW(), true),
    ('QNA', NOW(), NOW(), true),
    ('REVIEW', NOW(), NOW(), true),
    ('EMERGENCY_NOTE', NOW(), NOW(), true),
    ('TREATMENT_DIARY', NOW(), NOW(), true),
    ('HAPPY_MOMENT', NOW(), NOW(), true),
    ('SUPPORT', NOW(), NOW(), true),
    ('FREE_TALK', NOW(), NOW(), true);

-- 👤 2. Member (ID를 BINARY(16)으로 변환)
INSERT INTO member (id, user_id, email, password, phone, name, created_at, birthday, modified_at, activated, profile_url)
VALUES
    (UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), 'ajkd', 'doglover@example.com', 'pw1234', '010-4677-6023', '홍길동', NOW(), '2001-11-11', NOW(), true,'https://res.cloudinary.com/dyz2lq1f0/image/upload/v1764656860/%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C_modrhq.jpg'),
    (UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), 'user1', 'vetinfo@example.com', 'pw5678', '010-1234-5678', '홍길동', NOW(), '2001-11-11', NOW(), true, 'https://res.cloudinary.com/dyz2lq1f0/image/upload/v1764656851/%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C_2_koqas5.jpg'),
    (UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), 'user2', 'supporter@example.com', 'pw9999', '010-1234-7890', '홍길동', NOW(), '2001-11-11', NOW(), true, 'https://res.cloudinary.com/dyz2lq1f0/image/upload/v1764656843/%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C_1_fi2riw.jpg');

-- 📝 3. Post (게시글 - user_id 참조 부분 변환)
INSERT INTO post (board_id, user_id, title, content, created_at, modified_at, activated)
VALUES
    (1, UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), '영양제 추천 리스트', '우리 강아지에게 도움이 된 영양제를 공유합니다.', NOW(), NOW(), true),
    (2, UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), '강아지가 밥을 안 먹어요', '식욕이 떨어졌을 때 대처 방법이 궁금합니다.', NOW(), NOW(), true),
    (3, UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), '00동물병원 후기', '의사 선생님이 친절하고 꼼꼼하게 진료해주셨어요.', NOW(), NOW(), true),
    (4, UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), '응급 시 체온 유지 방법', '갑자기 쓰러졌을 때 이렇게 대처했어요.', NOW(), NOW(), true),
    (5, UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), '치료 3주차 일지', '오늘은 조금 기운이 돌아온 것 같아요.', NOW(), NOW(), true),
    (6, UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), '행복한 산책', '가을 바람을 맞으며 같이 산책했어요 🍂', NOW(), NOW(), true),
    (7, UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), '오늘은 유난히 힘드네요', '다른 분들도 이런 날 있으신가요?', NOW(), NOW(), true),
    (8, UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), '강아지용 옷 추천 좀요', '겨울 대비 따뜻한 옷 찾고 있어요!', NOW(), NOW(), true);

-- 🖼️ 4. PostImage (게시글 이미지 - post_id는 자동 증가값 가정)
-- (post_id가 UUID라면 여기도 UUID_TO_BIN을 써야 하지만, 보통 게시글 ID는 Long(숫자)을 쓰므로 그대로 둡니다.)
INSERT INTO post_image (post_id, origin_file_name, rename_file_name, save_path)
VALUES
    (3, 'hospital.jpg', 'hospital_1.jpg', 'https://res.cloudinary.com/dyz2lq1f0/image/upload/v1763707069/post_uploads/m1ryt6ptdy6wyydp4yog.png'),
    (6, 'walk.jpg', 'walk_1.jpg', 'https://res.cloudinary.com/dyz2lq1f0/image/upload/v1763706392/post_uploads/ajqvyjwdjvyy6be9ooav.png'),
    (8, 'dogclothes.jpg', 'dogclothes_1.jpg', 'https://res.cloudinary.com/dyz2lq1f0/image/upload/v1764656636/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7_2025-05-06_150107_mscbyh.png');

-- 🔖 5. PostHashtag (게시글 해시태그)
INSERT INTO post_hashtag (post_id, hashtag_content)
VALUES
    (1, '#영양제'),
    (1, '#면역력'),
    (3, '#병원후기'),
    (5, '#치료일지'),
    (7, '#위로'),
    (8, '#강아지옷');

-- 💬 6. Comments (댓글 - user_id 참조 부분 변환)
INSERT INTO comments (user_id, post_id, content, created_at, modified_at, activated)
VALUES
    (UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), 2, '식욕 촉진제 사용해보셨어요?', NOW(), NOW(), true),
    (UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), 2, '저도 비슷한 경험 있었어요.', NOW(), NOW(), true),
    (UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), 3, '좋은 병원이네요! 공유 감사해요.', NOW(), NOW(), true),
    (UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), 7, '힘내세요. 다들 같은 마음이에요.', NOW(), NOW(), true);

-- ❤️ 7. Like (좋아요 - user_id 참조 부분 변환)
INSERT INTO favorite (magazine_id, post_id, user_id, created_at, modified_at, activated)
VALUES
    (NULL, 3, UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), NOW(), NOW(), TRUE),
    (NULL, 5, UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), NOW(), NOW(), TRUE),
    (NULL, 6, UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), NOW(), NOW(), TRUE),
    (NULL, 7, UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), NOW(), NOW(), TRUE);
-- 1. gender 컬럼을 숫자가 아닌 문자로 변경
ALTER TABLE dog MODIFY gender VARCHAR(20);

-- 2. dog_size 컬럼도 문자로 변경
ALTER TABLE dog MODIFY dog_size VARCHAR(20);

INSERT INTO dog (user_id, dog_name, gender, is_neuered, breed, birth, disease, dog_size, created_at, modified_at, image_url, weight, is_rainbow)
VALUES
    -- 1. 첫 번째 회원 ('ajkd')의 강아지 : 뽀삐 (소형견, 말티푸)
    (
        UUID_TO_BIN('11111111-1111-1111-1111-111111111111'),
        '뽀삐',
        'MALE',
        true,
        '말티푸',
        '2020-05-05 00:00:00',
        false,
        'SMALL',
        NOW(),
        NOW(),
        'https://res.cloudinary.com/dyz2lq1f0/image/upload/v1764656860/%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C_modrhq.jpg',
        4.12,
        true
    ),

    -- 2. 두 번째 회원 ('user1')의 강아지 : 초코 (중형견, 푸들)
    (
        UUID_TO_BIN('22222222-2222-2222-2222-222222222222'),
        '초코',
        'FEMALE',
        false,
        '푸들',
        '2021-08-15 00:00:00',
        true,
        'MEDIUM',
        NOW(),
        NOW(),
        'https://res.cloudinary.com/dyz2lq1f0/image/upload/v1764656860/%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C_modrhq.jpg',
        3.12,
        true
    ),

    -- 3. 세 번째 회원 ('user2')의 강아지 : 맥스 (대형견, 리트리버)
    (
        UUID_TO_BIN('33333333-3333-3333-3333-333333333333'),
        '맥스',
        'MALE',
        true,
        '골든 리트리버',
        '2019-01-10 00:00:00',
        false,
        'LARGE',
        NOW(),
        NOW(),
        'https://res.cloudinary.com/dyz2lq1f0/image/upload/v1764656860/%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C_modrhq.jpg',
        5.1,
        false
    );