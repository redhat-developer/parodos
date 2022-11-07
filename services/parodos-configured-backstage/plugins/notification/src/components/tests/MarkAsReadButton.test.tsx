/*
 *  Copyright (c) 2022 Red Hat Developer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/**
 * @author Richard Wang (Github: RichardW98)
 */

import React from 'react';
import renderer from 'react-test-renderer';
import MarkAsReadButton from '../components/notifications/NotificationActionsHeader/buttons/MarkAsReadButton';
import NotificationProvider from '../context/notifications';
import { ToastProvider } from '../context/toast';

describe('MarkAsReadButton', () => {
  it('MarkAsReadButton should match stored snapshot', () => {
    const tree = renderer
      .create(
        <ToastProvider>
          <NotificationProvider>
            <MarkAsReadButton />
          </NotificationProvider>
        </ToastProvider>,
      )
      .toJSON();
    expect(tree).toMatchSnapshot();
  });
});