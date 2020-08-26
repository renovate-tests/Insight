package com.meemaw.auth.tfa.setup.model;

import com.meemaw.auth.tfa.TfaMethod;
import com.meemaw.auth.tfa.setup.dto.TfaSetupDTO;
import java.time.OffsetDateTime;
import java.util.UUID;

public interface TfaSetup {

  UUID getUserId();

  OffsetDateTime getCreatedAt();

  TfaMethod getMethod();

  default TfaSetupDTO dto() {
    return new TfaSetupDTO(getCreatedAt(), getMethod());
  }
}